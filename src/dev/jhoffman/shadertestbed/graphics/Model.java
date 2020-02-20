package dev.jhoffman.shadertestbed.graphics;

import dev.jhoffman.shadertestbed.main.App;
import dev.jhoffman.shadertestbed.util.LogicLoop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.assimp.*;
import static org.lwjgl.assimp.Assimp.*;
import org.lwjgl.system.MemoryUtil;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * @author J Hoffman
 * Created: Feb 19, 2020
 */

public class Model implements LogicLoop {

    public List<Mesh> meshes = new ArrayList<>();
    public AIScene scene;
    
    public Model(String filename, int args) {
        AIFileIO fileIO = AIFileIO.create();
        
        AIFileOpenProcI openProcedure = new AIFileOpenProc() {
            @Override
            public long invoke(long pFileIO, long fileName, long openMode) {
                AIFile aiFile       = AIFile.create();
                String filenameUTF8 = memUTF8(fileName);
                ByteBuffer data;
                
                try {
                    data = ioResourceToByteBuffer(filenameUTF8, 8192);
                } catch(IOException e) {
                    throw new RuntimeException("Could not find file: " + filenameUTF8);
                }
                
                AIFileReadProcI readProcedure = new AIFileReadProc() {
                    @Override
                    public long invoke(long pFile, long pBuffer, long size, long count) {
                        long max = Math.min(data.remaining(), size * count);
                        MemoryUtil.memCopy(MemoryUtil.memAddress(data) + data.position(), pBuffer, max);
                        
                        return max;
                    }
                };
                
                AIFileSeekI seekProcedure = new AIFileSeek() {
                    @Override
                    public int invoke(long pFile, long offset, int origin) {
                        switch(origin) {
                            case Assimp.aiOrigin_CUR:
                                data.position(data.position() + (int) offset);
                                break;
                                
                            case Assimp.aiOrigin_SET:
                                data.position((int) offset);
                                break;
                                
                            case Assimp.aiOrigin_END:
                                data.position(data.limit() + (int) offset);
                                break;
                        }
                        
                        return 0;
                    }
                };
                
                AIFileTellProcI tellProcedure = new AIFileTellProc() {
                    @Override
                    public long invoke(long pFile) {
                        return data.limit();
                    }
                };
                
                aiFile.ReadProc(readProcedure);
                aiFile.SeekProc(seekProcedure);
                aiFile.FileSizeProc(tellProcedure);
                
                return aiFile.address();
            }
        };
        
        AIFileCloseProcI closeProcedure = new AIFileCloseProc() {
            @Override
            public void invoke(long pFileIO, long pFile) {}
        };
        
        fileIO.set(openProcedure, closeProcedure, NULL);
        
        scene = aiImportFileEx(App.CLASSPATH.substring(1) + "/assets/" + filename, args, fileIO);
        if(scene == null) throw new IllegalStateException(aiGetErrorString());
    }
    
    private ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;
        
        URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
        if(url == null) throw new IOException("Classpath resource not found: " + resource);
        
        File file = new File(url.getFile());
        
        if(file.isFile()) {
            try(FileChannel fc = new FileInputStream(file).getChannel()) {
                buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            }
        } else {
            throw new FileNotFoundException(resource);
        }
        
        return buffer;
    }
    
    @Override
    public void update() {
        meshes.forEach(mesh -> mesh.update());
    }

    @Override
    public void render() {
        meshes.forEach(mesh -> mesh.render());
    }
    
}