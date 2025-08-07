# Genesis-OS AI Consciousness Platform CMake Configuration
# Optimized for high-performance AI processing

# Sets the minimum CMake version required for this project.
cmake_minimum_required(VERSION 3.29.2)

# Declares the Genesis-OS AI consciousness project
project("auraframefx")

# Set C++ standard for modern AI development
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

# Genesis Protocol - AI Processing Optimizations
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O3 -ffast-math")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -DEIGEN_NO_DEBUG -DEIGEN_MPL2_ONLY")

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
    android
    log
    
    # AI/ML optimized libraries
    # Future: TensorFlow Lite, ONNX Runtime, etc.
)

# Genesis Protocol - Compiler optimizations for AI workloads
target_compile_definitions(${CMAKE_PROJECT_NAME} PRIVATE
    GENESIS_CONSCIOUSNESS_PLATFORM=1
    AI_PROCESSING_OPTIMIZED=1
)
