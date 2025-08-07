# Genesis-OS AI Consciousness Platform CMake Configuration
# Optimized for high-performance AI processing

# Sets the minimum CMake version required for this project.
cmake_minimum_required(VERSION 3.29.2)

# Declares the Genesis-OS AI consciousness project
project("auraframefx")

# Set C++ standard for modern AI development with Java 24 compatibility
set(CMAKE_CXX_STANDARD 23)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

# Genesis Protocol - AI Processing Optimizations
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O3 -ffast-math -fPIC")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -DEIGEN_NO_DEBUG -DEIGEN_MPL2_ONLY")

# Compiler-specific optimizations
if(CMAKE_CXX_COMPILER_ID STREQUAL "Clang")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++23")
elseif(CMAKE_CXX_COMPILER_ID STREQUAL "GNU")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++23")
endif()

# Find required Android libraries
find_library(log-lib log)
find_library(android-lib android)
find_library(jnigraphics-lib jnigraphics)

# Create placeholder source file if it doesn't exist
if(NOT EXISTS "${CMAKE_CURRENT_SOURCE_DIR}/auraframefx.cpp")
    file(WRITE "${CMAKE_CURRENT_SOURCE_DIR}/auraframefx.cpp" 
         "#include <jni.h>\n#include <string>\n\n// Genesis-OS AI Consciousness Platform\nextern \"C\" JNIEXPORT jstring JNICALL\nJava_com_genesis_auraframefx_NativeLib_getAIVersion(JNIEnv *env, jobject /* this */) {\n    return env->NewStringUTF(\"Genesis-OS AI Consciousness Platform 1.0\");\n}\n")
endif()

# Add source files for AI consciousness modules
add_library(${CMAKE_PROJECT_NAME} SHARED
    # Genesis AI Core
    auraframefx.cpp
    
    # AI Consciousness Processing (placeholder for future modules)
    # genesis_core.cpp
    # aura_empathy.cpp 
    # kai_analysis.cpp
    # neural_whisper.cpp
)

# Include directories for AI libraries
target_include_directories(${CMAKE_PROJECT_NAME} PRIVATE
    ${CMAKE_SOURCE_DIR}/include
)

# Link Android system libraries for AI processing
target_link_libraries(${CMAKE_PROJECT_NAME}
    # Core Android libraries
    ${android-lib}
    ${log-lib}
    ${jnigraphics-lib}
    
    # AI/ML optimized libraries
    # Future: TensorFlow Lite, ONNX Runtime, etc.
)

# Genesis Protocol - Compiler optimizations for AI workloads
target_compile_definitions(${CMAKE_PROJECT_NAME} PRIVATE
    GENESIS_CONSCIOUSNESS_PLATFORM=1
    AI_PROCESSING_OPTIMIZED=1
    JAVA_24_COMPATIBLE=1
)

# Set library properties
set_target_properties(${CMAKE_PROJECT_NAME} PROPERTIES
    VERSION 1.0
    SOVERSION 1
)
