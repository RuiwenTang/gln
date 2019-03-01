package gln.buffer

import glm_.BYTES
import glm_.L
import glm_.mat4x4.Mat4
import glm_.vec3.Vec3
import glm_.vec4.Vec4
import gln.*
import gln.Usage.Companion.STATIC_DRAW
import gln.objects.GlBuffer
import kool.adr
import kool.pos
import org.lwjgl.opengl.GL15C
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL30C
import org.lwjgl.opengl.GL45C
import org.lwjgl.system.MemoryUtil.NULL
import java.nio.*


object GlBufferDsl {

    var target: BufferTarget = BufferTarget.ARRAY
    var buffer = GlBuffer()

    fun data(data: ShortArray, usage: Usage = STATIC_DRAW) = GL15C.glBufferData(target.i, data, usage.i)
    fun data(data: IntArray, usage: Usage = STATIC_DRAW) = GL15C.glBufferData(target.i, data, usage.i)
    fun data(data: LongArray, usage: Usage = STATIC_DRAW) = GL15C.glBufferData(target.i, data, usage.i)
    fun data(data: FloatArray, usage: Usage = STATIC_DRAW) = GL15C.glBufferData(target.i, data, usage.i)
    fun data(data: DoubleArray, usage: Usage = STATIC_DRAW) = GL15C.glBufferData(target.i, data, usage.i)

    fun data(data: ByteBuffer, usage: Usage = STATIC_DRAW) = GL15C.nglBufferData(target.i, data.remaining().L, data.adr + data.pos, usage.i)
    fun data(data: ShortBuffer, usage: Usage = STATIC_DRAW) = GL15C.nglBufferData(target.i, data.remaining().L * Short.BYTES, data.adr + data.pos * Short.BYTES, usage.i)
    fun data(data: IntBuffer, usage: Usage = STATIC_DRAW) = GL15C.nglBufferData(target.i, data.remaining().L * Int.BYTES, data.adr + data.pos * Int.BYTES, usage.i)
    fun data(data: LongBuffer, usage: Usage = STATIC_DRAW) = GL15C.nglBufferData(target.i, data.remaining().L * Long.BYTES, data.adr + data.pos * Long.BYTES, usage.i)
    fun data(data: FloatBuffer, usage: Usage = STATIC_DRAW) = GL15C.nglBufferData(target.i, data.remaining().L * Float.BYTES, data.adr + data.pos * Float.BYTES, usage.i)
    fun data(data: DoubleBuffer, usage: Usage = STATIC_DRAW) = GL15C.nglBufferData(target.i, data.remaining().L * Double.BYTES, data.adr + data.pos * Double.BYTES, usage.i)

    fun data(size: Int, data: Vec4, usage: Usage = STATIC_DRAW) {
        buf.putFloat(0, data.x).putFloat(Float.BYTES, data.y).putFloat(Float.BYTES * 2, data.z).putFloat(Float.BYTES * 3, data.w)
        GL15C.nglBufferData(target.i, size.L, bufAd, usage.i)
    }

    fun data(size: Int, usage: Usage = STATIC_DRAW) = GL15C.nglBufferData(target.i, size.L, NULL, usage.i)

    fun subData(offset: Int, data: ByteBuffer) = GL15C.nglBufferSubData(target.i, offset.L, data.remaining().L, data.adr + data.pos)
    fun subData(offset: Int, data: ShortBuffer) = GL15C.nglBufferSubData(target.i, offset.L, data.remaining().L * Short.BYTES, data.adr + data.pos * Short.BYTES)
    fun subData(offset: Int, data: IntBuffer) = GL15C.nglBufferSubData(target.i, offset.L, data.remaining().L * Int.BYTES, data.adr + data.pos * Int.BYTES)
    fun subData(offset: Int, data: LongBuffer) = GL15C.nglBufferSubData(target.i, offset.L, data.remaining().L * Long.BYTES, data.adr + data.pos * Long.BYTES)
    fun subData(offset: Int, data: FloatBuffer) = GL15C.nglBufferSubData(target.i, offset.L, data.remaining().L * Float.BYTES, data.adr + data.pos * Float.BYTES)
    fun subData(offset: Int, data: DoubleBuffer) = GL15C.nglBufferSubData(target.i, offset.L, data.remaining().L * Double.BYTES, data.adr + data.pos * Double.BYTES)

    fun subData(data: ByteBuffer) = GL15C.nglBufferSubData(target.i, 0L, data.remaining().L, data.adr + data.pos)
    fun subData(data: ShortBuffer) = GL15C.nglBufferSubData(target.i, 0L, data.remaining().L * Short.BYTES, data.adr + data.pos * Short.BYTES)
    fun subData(data: IntBuffer) = GL15C.nglBufferSubData(target.i, 0L, data.remaining().L * Int.BYTES, data.adr + data.pos * Int.BYTES)
    fun subData(data: LongBuffer) = GL15C.nglBufferSubData(target.i, 0L, data.remaining().L * Long.BYTES, data.adr + data.pos * Long.BYTES)
    fun subData(data: FloatBuffer) = GL15C.nglBufferSubData(target.i, 0L, data.remaining().L * Float.BYTES, data.adr + data.pos * Float.BYTES)
    fun subData(data: DoubleBuffer) = GL15C.nglBufferSubData(target.i, 0L, data.remaining().L * Double.BYTES, data.adr + data.pos * Double.BYTES)

    fun data(data: Vec3, usage: Usage = STATIC_DRAW) = GL15C.glBufferData(target.i, data to buf, usage.i)

    // ----- Mat4 -----
    fun data(mat: Mat4, usage: Usage) {
        mat to buf
        GL15C.nglBufferData(target.i, Mat4.size.L, bufAd, usage.i)
    }

    fun subData(offset: Int, mat: Mat4) {
        mat to buf
        GL15C.nglBufferSubData(target.i, offset.L, Mat4.size.L, bufAd)
    }

    fun subData(mat: Mat4) {
        mat to buf
        GL15C.nglBufferSubData(target.i, 0L, Mat4.size.L, bufAd)
    }

    fun bindRange(index: Int, offset: Int, size: Int) = GL30C.glBindBufferRange(target.i, index, buffer.name, offset.L, size.L)

    fun bindBase(index: Int) = GL30.glBindBufferBase(target.i, index, 0)

    fun mapRange(length: Int, access: Int) = mapRange(0, length, access)
    fun mapRange(offset: Int, length: Int, access: Int) = GL30.glMapBufferRange(target.i, offset.L, length.L, access)

    inline fun mappedRange(size: Int, flags: Int, block: (ByteBuffer?) -> Unit) = mappedRange(0, size, flags, block)
    inline fun mappedRange(offset: Int, size: Int, flags: Int, block: (ByteBuffer?) -> Unit) {
        block(GL30C.glMapBufferRange(target.i, offset.L, size.L, flags))
        GL30C.glUnmapBuffer(target.i)
    }

    fun flushRange(length: Int) = flushRange(0, length)
    fun flushRange(offset: Int, length: Int) = GL30.glFlushMappedBufferRange(target.i, offset.L, length.L)

    fun unmap() = GL15C.glUnmapBuffer(target.i)
}

inline fun glGenBuffers(bufferName: IntBuffer, block: GlBuffersDsl.() -> Unit) {
    GL15C.glGenBuffers(bufferName)
    GlBuffersDsl.names = bufferName
    GlBuffersDsl.block()
}

object GlBuffersDsl {

    lateinit var names: IntBuffer
//    var target = BufferTarget.ARRAY

    fun <E> E.bind(target: BufferTarget) where E : Enum<E>, E : GlBufferEnum = GL15C.glBindBuffer(target.i, names[ordinal])
    fun unbind(target: BufferTarget) = GL15C.glBindBuffer(target.i, 0)
    inline fun <E> E.bind(target: BufferTarget, block: GlBufferDsl.() -> Unit) where E : Enum<E>, E : GlBufferEnum {
        bind(target)
        GlBufferDsl.target = target
        GlBufferDsl.buffer = GlBuffer(names[ordinal])
        GlBufferDsl.block()
    }
    inline fun <E> E.bound(target: BufferTarget, block: GlBufferDsl.() -> Unit) where E : Enum<E>, E : GlBufferEnum {
        bind(target, block)
        GL15C.glBindBuffer(target.i, 0)
    }

    fun <E> E.bindBase(target: BufferTarget, index: Int) where E : Enum<E>, E : GlBufferEnum = GL30C.glBindBufferBase(target.i, index, names[ordinal])

    fun <E> E.storage(size: Int, flags: Int = 0) where E : Enum<E>, E : GlBufferEnum = GL45C.glNamedBufferStorage(names[ordinal], size.L, flags)

    fun <E> E.storage(data: ByteBuffer, flags: Int = 0) where E : Enum<E>, E : GlBufferEnum = GL45C.glNamedBufferStorage(names[ordinal], data, flags)
    fun <E> E.storage(data: ShortBuffer, flags: Int = 0) where E : Enum<E>, E : GlBufferEnum = GL45C.glNamedBufferStorage(names[ordinal], data, flags)
    fun <E> E.storage(data: FloatBuffer, flags: Int = 0) where E : Enum<E>, E : GlBufferEnum = GL45C.glNamedBufferStorage(names[ordinal], data, flags)

    fun <E> E.mapRange(offset: Int, size: Int, flags: Int = 0) where E : Enum<E>, E : GlBufferEnum = GL45C.glMapNamedBufferRange(names[ordinal], offset.L, size.L, flags)
    fun <E> E.mapRange(size: Int, flags: Int = 0) where E : Enum<E>, E : GlBufferEnum = GL45C.glMapNamedBufferRange(names[ordinal], 0L, size.L, flags)
}