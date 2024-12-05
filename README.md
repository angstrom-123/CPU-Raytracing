# CPU Raytracing
Toy CPU path tracer written entirely in Java.

## Features
- Physically based path tracing
- BVH optimisation using axis-aligned bounding boxes
- Support for Lambertian diffuse, metallic, glass, and emissive materials
- .obj file format model importing (model and normals only)
- Texture support
- Custom image texture support for spheres

## Running
Requires an installation of at least JDK-21.

By default, rendered images will attempt to save to a "renders" folder in the current or nearby directory. If this is not found, the default location is the cwd.

## Config
**Multithreading:**
Preliminary tests have shown that, on powerful computers, thread count should be equal to the number of high-power CPU cores (such as Intel's "performance cores"). Weaker systems and laptops could experience best performance with just one thread. 

**Tiling:**
Tile width width should be left at auto for fastest results as this optimizes the cache. Tile height can be reduced to maintain a higher average thread count throughout the render (as when one tile is finished, the thread can be reassigned).

![configMenu](https://github.com/user-attachments/assets/e64ebafa-d965-44f5-941a-50e41388c789)
## Demos
![random balls](https://github.com/user-attachments/assets/d9db3905-89b6-47df-94d3-75e463d5e949)

![glass knight](https://github.com/user-attachments/assets/13c44231-3fad-4e35-b30f-d2be8eb933f4)

![7559715668385661](https://github.com/user-attachments/assets/2d54ef4c-551b-4be9-a45a-6506f0882a25)

![emission](https://github.com/user-attachments/assets/5a56ea64-5ccd-4ab2-9b1a-faaec0b39fc5)

![cornell](https://github.com/user-attachments/assets/27ee759a-580d-4e6d-9cf5-11fc5f9dba79)

## References
"Ray tracing in one weekend" and "Ray tracing the next week" : https://raytracing.github.io/
