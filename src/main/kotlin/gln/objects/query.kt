package gln.objects

import gln.QueryTarget
import gln.gl
import kool.Adr
import kool.IntBuffer
import kool.adr
import kool.rem
import org.lwjgl.opengl.GL15C
import org.lwjgl.opengl.GL20C
import java.nio.IntBuffer

inline class GlQuery(val name: Int) {

    // --- [ glBeginQuery ] ---

    infix fun begin(target: QueryTarget) = GL15C.glBeginQuery(target.i, name)

    // --- [ glEndQuery ] ---

    infix fun end(target: QueryTarget) = GL15C.glEndQuery(target.i)

    fun <R> use(target: QueryTarget, block: (GlQuery) -> R): R {
        GL15C.glBeginQuery(target.i, name)
        return block(this).also { GL15C.glEndQuery(target.i) }
    }

    // --- [ glIsQuery ] ---

    val valid: Boolean
        get() = GL20C.glIsQuery(name)

    companion object {

        // --- [ glCreateQueries ] ---

        infix fun create(target: QueryTarget): GlQuery = gl.createQueries(target)
    }
}

fun GlQueries(size: Int): GlQueries = GlQueries(IntBuffer(size))

inline class GlQueries(val i: IntBuffer) {

    inline val rem: Int
        get() = i.rem

    inline val adr: Adr
        get() = i.adr

    // --- [ glCreateQueries ] ---

    infix fun create(target: QueryTarget) = gl.createQueries(target, this)

    companion object {

        // --- [ glCreateQueries ] ---

        fun create(target: QueryTarget, size: Int): GlQueries = gl.createQueries(target, size)
    }
}