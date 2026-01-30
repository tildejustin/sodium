package me.jellysquid.mods.sodium.client.model.vertex.formats.line;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.model.vertex.VertexSink;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormatElement;
import net.minecraft.client.render.VertexFormats;

public interface LineVertexSink extends VertexSink {
    VertexFormat VERTEX_FORMAT = new VertexFormat(ImmutableList.<VertexFormatElement>builder().add(VertexFormats.POSITION_ELEMENT).add(VertexFormats.COLOR_ELEMENT).add(VertexFormats.NORMAL_ELEMENT).add(VertexFormats.PADDING_ELEMENT).build());

    /**
     * Writes a line vertex to the sink.
     * @param x The x-position of the vertex
     * @param y The y-position of the vertex
     * @param z The z-position of the vertex
     * @param color The ABGR-packed color of the vertex
     * @param normal The 3 byte packed normal vector of the vertex
     */
    void vertexLine(float x, float y, float z, int color, int normal);
}
