package net.atired.creaturefeature.misc;

import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CODE ADAPTED FROM https://winter.dev/projects/mesh/icosphere ON C++
 * THEY HAVE PEAK PROJECTS I RECKON
 * Functions for model generation.
 */
public class IcoSphere {


    private static float Z = (float) ((1.0f + Math.sqrt(5.0f)) / 2.0f); // Golden ratio
    private static Vector2f UV = new Vector2f(0.1f,0.1f); // The UV coordinates are laid out in a 11x3 grid

    private static int IcoVertexCount = 22;
    private static int IcoIndexCount = 60;

    private static Vec3 IcoVerts[] = new Vec3[]{
            new Vec3( 0, -1, -Z), new Vec3(-1, -Z,  0), new Vec3( Z,  0, -1), new Vec3( 1, -Z,  0),
            new Vec3( 1,  Z,  0), new Vec3(-1, -Z,  0), new Vec3( Z,  0,  1), new Vec3( 0, -1,  Z),
            new Vec3( 1,  Z,  0), new Vec3(-1, -Z,  0), new Vec3( 0,  1,  Z), new Vec3(-Z,  0,  1),
            new Vec3( 1,  Z,  0), new Vec3(-1, -Z,  0), new Vec3(-1,  Z,  0), new Vec3(-Z,  0, -1),
            new Vec3( 1,  Z,  0), new Vec3(-1, -Z,  0), new Vec3( 0,  1, -Z), new Vec3( 0, -1, -Z),
            new Vec3( 1,  Z,  0), new Vec3( Z,  0, -1)
    };

    private static final Vector2f[] IcoUvs = new Vector2f[]{
            new Vector2f( 0, 1), //  0
            new Vector2f( 1, 0), //  1
            new Vector2f( 1, 2), //  2  //
            new Vector2f( 2, 1), //  3  // Vertices & UVs are ordered by U then V coordinates,
            new Vector2f( 2, 3), //  4  //
            new Vector2f( 3, 0), //  5  //        4     8    12    16    20
            new Vector2f( 3, 2), //  6  //      /  \  /  \  /  \  /  \  /  \
            new Vector2f( 4, 1), //  7  //     2---- 6----10----14----18----21
            new Vector2f( 4, 3), //  8  //   /  \  /  \  /  \  /  \  /  \  /
            new Vector2f( 5, 0), //  9  //  0---- 3---- 7----11----15----19
            new Vector2f( 5, 2), // 10  //   \  /  \  /  \  /  \  /  \  /
            new Vector2f( 6, 1), // 11  //     1     5     9    13    17
            new Vector2f( 6, 3), // 12  //
            new Vector2f( 7, 0), // 13  // [4, 8, 12, 16, 20] have the same position
            new Vector2f( 7, 2), // 14  // [1, 5, 9, 13, 17]  have the same position
            new Vector2f( 8, 1), // 15  // [0, 19]            have the same position
            new Vector2f( 8, 3), // 16  // [2, 21]            have the same position
            new Vector2f( 9, 0), // 17  //
            new Vector2f( 9, 2), // 18
            new Vector2f(10, 1), // 19
            new Vector2f(10, 3), // 20
            new Vector2f(11, 2)  // 21
    };

    private static int IcoIndex[] = {
            2,  6,  4, // Top
            6, 10,  8,
            10, 14, 12,
            14, 18, 16,
            18, 21, 20,

            0,  3,  2, // Middle
            2,  3,  6,
            3,  7,  6,
            6,  7, 10,
            7, 11, 10,
            10, 11, 14,
            11, 15, 14,
            14, 15, 18,
            15, 19, 18,
            18, 19, 21,

            0,  1,  3, // Bottom
            3,  5,  7,
            7,  9, 11,
            11, 13, 15,
            15, 17, 19
    };
    public List<Vec3> pos = new ArrayList<>();
    public List<Vector2f> uvs = new ArrayList<>();
    public List<Integer> index = new ArrayList<>();
    private IcoSphere(){

    }
    public static IcoSphere MakeIcosphere(int resolution) {
        // For each resolution, every triangle is subdivided and replaced with 4 new triangles.
        // Most of the vertices are shared between triangles, so an index is also generated.
        // The number of vertices and indices for a given resolution can be calculated by using a geometric series.
        //
        // Example:
        //
        //     *-------*               *---*---*
        //    / \     /               / \ / \ /
        //   /   \   /    ------->   *---*---*
        //  /     \ /               / \ / \ /
        // *-------*               *---*---*

	int rn = (int)Math.pow(4, resolution);
	int totalIndexCount = IcoIndexCount * rn;
	int totalVertexCount = IcoVertexCount + IcoIndexCount * (1 - rn) / (1 - 4);

        IcoSphere sphere = new IcoSphere();
        for (int i = 0; i < IcoVerts.length; i++) {  // Copy in initial mesh
            sphere.pos.add(i, IcoVerts[i]);
            System.out.println(IcoUvs[i].x + " " + IcoUvs[i].y + "   " + i);
            sphere.uvs.add(i, IcoUvs[i]);
        }

        for (int i = 0; i < IcoIndexCount; i++) {
            sphere.index.add(i, IcoIndex[i]);
        }

        int currentIndexCount = IcoIndexCount;
        int currentVertCount = IcoVertexCount;

        for (int r = 0; r < resolution; r++) {
            // Now split the triangles.
            // This can be done in place, but needs to keep track of the unique triangles
            //
            //     i+2                 i+2
            //    /   \               /  \
            //   /     \    ---->   m2----m1
            //  /       \          /  \  /  \
            // i---------i+1      i----m0----i+1

            Map<Long, Integer> triangleFromEdge = new HashMap<>();
            int indexCount = currentIndexCount;

            for (int t = 0; t < indexCount; t += 3) {
                int[] midpoints = new int[3];

                for (int e = 0; e < 3; e++) {
                    int first = sphere.index.get(t + e);
                    int second = sphere.index.get(t + (t + e + 1) % 3);
                    if (first > second) {
                        int temp = first;
                        first = second;
                        second = temp;
                    }
                    long hash = (long)first | ((long)second << (32));
                    boolean a = false;
                    if(triangleFromEdge.get(hash)==null){
                        triangleFromEdge.putIfAbsent(hash, currentVertCount);
                        a = triangleFromEdge.containsKey(hash);
                    }

                    if (true) {
                        sphere.pos.add(currentVertCount, (sphere.pos.get(first).add(sphere.pos.get(second))).scale(0.5f));
                        sphere.uvs.add(currentVertCount, (sphere.uvs.get(first).add(sphere.uvs.get(second))).mul(0.5f));
                        midpoints[e] = currentVertCount;
                        currentVertCount += 1;
                    }

                }

                int mid0 = midpoints[0];
                int mid1 = midpoints[1];
                int mid2 = midpoints[2];

                sphere.index.add(currentIndexCount++, sphere.index.get(t));
                sphere.index.add(currentIndexCount++, mid0);
                sphere.index.add(currentIndexCount++, mid2);

                sphere.index.add(currentIndexCount++, sphere.index.get(t + 1));
                sphere.index.add(currentIndexCount++, mid1);
                sphere.index.add(currentIndexCount++, mid0);

                sphere.index.add(currentIndexCount++, sphere.index.get(t + 2));
                sphere.index.add(currentIndexCount++, mid2);
                sphere.index.add(currentIndexCount++, mid1);

                sphere.index.set(t, mid0); // Overwrite the original triangle with the 4th new triangle
                sphere.index.set(t + 1, mid1);
                sphere.index.set(t + 2, mid2);
            }
        }

        // Normalize all the positions to create the sphere
        int i = 0;
        for (Vec3 pos : sphere.pos) {
            pos = pos.normalize();
            sphere.pos.set(i,pos);
            i+=1;
        }
        return sphere;
    }

}