@file:Suppress("NOTHING_TO_INLINE")

package gln.renderbuffer

import glm_.vec2.Vec2i
import gln.GetRenderbuffer
import gln.gl
import kool.IntBuffer
import kool.adr
import kool.get
import kool.rem
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL30C
import java.nio.IntBuffer
import kotlin.properties.Delegates


var renderbufferName: IntBuffer by Delegates.notNull()


inline fun glRenderbufferStorageMultisample(samples: Int, internalFormat: Int, size: Vec2i) = GL30.glRenderbufferStorageMultisample(GL30.GL_RENDERBUFFER, samples, internalFormat, size.x, size.y)

inline fun glBindRenderbuffer() = GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0)
inline fun glBindRenderbuffer(renderbuffer: IntArray) = GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, renderbuffer[0])
inline fun glBindRenderbuffer(renderbuffer: IntBuffer) = GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, renderbuffer[0])

inline fun glRenderbufferStorage(internalFormat: Int, width: Int, height: Int) = GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, internalFormat, width, height)
inline fun glRenderbufferStorage(internalFormat: Int, size: Vec2i) = GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, internalFormat, size.x, size.y)
inline fun glRenderbufferStorage(target: Int, internalFormat: Int, size: Vec2i) = GL30.glRenderbufferStorage(target, internalFormat, size.x, size.y)


inline class GlRenderbuffers(val names: IntBuffer) {
    val rem get() = names.rem
    val adr get() = names.adr

    // --- [ glCreateRenderbuffers ] ---

    /**
     * Returns {@code n} previously unused renderbuffer names in {@code renderbuffers}, each representing a new renderbuffer object.
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glCreateRenderbuffers">Reference Page</a>
     */
    fun create() = gl.createRenderbuffers(this)
}

fun GlRenderbuffers(size: Int) = GlRenderbuffers(IntBuffer(size))

inline class GlRenderbuffer(val name: Int = -1) {

    inline fun bind(block: RenderBuffer.() -> Unit): GlRenderbuffer {
        GL30C.glBindRenderbuffer(GL30C.GL_RENDERBUFFER, name)
        RenderBuffer.block()
        GL30C.glBindRenderbuffer(GL30C.GL_RENDERBUFFER, 0)
        return this
    }

    fun delete() = GL30C.glDeleteRenderbuffers(name)


    // --- [ glGetNamedRenderbufferParameteriv ] ---

    /**
     * DSA version of {@link GL30C#glGetRenderbufferParameteriv GetRenderbufferParameteriv}.
     *
     * @param name the parameter whose value to retrieve from the renderbuffer bound to {@code target}. One of:<br><table><tr><td>{@link GL30#GL_RENDERBUFFER_WIDTH RENDERBUFFER_WIDTH}</td><td>{@link GL30#GL_RENDERBUFFER_HEIGHT RENDERBUFFER_HEIGHT}</td><td>{@link GL30#GL_RENDERBUFFER_INTERNAL_FORMAT RENDERBUFFER_INTERNAL_FORMAT}</td></tr><tr><td>{@link GL30#GL_RENDERBUFFER_RED_SIZE RENDERBUFFER_RED_SIZE}</td><td>{@link GL30#GL_RENDERBUFFER_GREEN_SIZE RENDERBUFFER_GREEN_SIZE}</td><td>{@link GL30#GL_RENDERBUFFER_BLUE_SIZE RENDERBUFFER_BLUE_SIZE}</td></tr><tr><td>{@link GL30#GL_RENDERBUFFER_ALPHA_SIZE RENDERBUFFER_ALPHA_SIZE}</td><td>{@link GL30#GL_RENDERBUFFER_DEPTH_SIZE RENDERBUFFER_DEPTH_SIZE}</td><td>{@link GL30#GL_RENDERBUFFER_STENCIL_SIZE RENDERBUFFER_STENCIL_SIZE}</td></tr><tr><td>{@link GL30#GL_RENDERBUFFER_SAMPLES RENDERBUFFER_SAMPLES}</td></tr></table>
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glGetRenderbufferParameter">Reference Page</a>
     */
    infix fun getParameter(name: GetRenderbuffer): Int = gl.getRenderbufferParameter(this, name)

    // --- [ glNamedRenderbufferStorage ] ---

    /**
     * DSA version of {@link GL30C#glRenderbufferStorage RenderbufferStorage}.
     *
     * @param internalFormat the internal format to use for the renderbuffer object's image. Must be a color-renderable, depth-renderable, or stencil-renderable format.
     * @param size           the size of the renderbuffer, in pixels
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glRenderbufferStorage">Reference Page</a>
     */
    fun storage(internalFormat: Int, size: Vec2i) = gl.renderbufferStorage(this, internalFormat, size)

    // --- [ glNamedRenderbufferStorageMultisample ] ---

    /**
     * DSA version of {@link GL30C#glRenderbufferStorageMultisample RenderbufferStorageMultisample}.
     *
     * @param samples        the number of samples to be used for the renderbuffer object's storage
     * @param internalFormat the internal format to use for the renderbuffer object's image. Must be a color-renderable, depth-renderable, or stencil-renderable format.
     * @param width          the width of the renderbuffer, in pixels
     * @param height         the height of the renderbuffer, in pixels
     *
     * @see <a target="_blank" href="http://docs.gl/gl4/glRenderbufferStorageMultisample">Reference Page</a>
     */
    fun storageMS(samples: Int, internalFormat: Int, size: Vec2i) = gl.renderbufferStorageMS(this, samples, internalFormat, size)

    companion object {

        // --- [ glCreateRenderbuffers ] ---

        /**
         * Returns {@code n} previously unused renderbuffer names in {@code renderbuffers}, each representing a new renderbuffer object.
         *
         * @see <a target="_blank" href="http://docs.gl/gl4/glCreateRenderbuffers">Reference Page</a>
         */
        fun create(): GlRenderbuffer = gl.createRenderbuffers()

        fun gen(): GlRenderbuffer = GlRenderbuffer(GL30C.glGenRenderbuffers())
    }
}