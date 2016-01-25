package com.blueskiron.rxsamples.files;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.jasypt.util.binary.BasicBinaryEncryptor;

public class Utils {

  private static final Deflater deflater = new Deflater();
  private static final Inflater inflater = new Inflater();
  
  public static final int DEFAULT_MAX_BYTES_PER_EMISSION = 8192;

  private static final BasicBinaryEncryptor encryptor = new BasicBinaryEncryptor();

  static {
    encryptor.setPassword("we are going reactive!");
  }

  public static byte[] compress(byte[] data) throws IOException {
    deflater.setInput(data);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
    deflater.finish();
    byte[] buffer = new byte[data.length];
    while (!deflater.finished()) {
      int count = deflater.deflate(buffer); // returns the generated
      outputStream.write(buffer, 0, count);
    }
    outputStream.close();
    deflater.reset();
    byte[] output = outputStream.toByteArray();
    //System.out.println("compressed: " + java.util.Arrays.toString(output));
    return output;
  }

  public static byte[] decompress(byte[] data) throws IOException, DataFormatException {
    inflater.setInput(data);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
    byte[] buffer = new byte[data.length];
    while (!inflater.finished()) {
      int count = inflater.inflate(buffer);
      outputStream.write(buffer, 0, count);
    }
    outputStream.close();
    inflater.reset();
    byte[] output = outputStream.toByteArray();
    //System.out.println("decompressed: " + java.util.Arrays.toString(output));
    return output;
  }
  
  public static byte[] encrypt(byte[] source){
    return encryptor.encrypt(source);
  }
  
  public static byte[] decrypt(byte[] source){
    return encryptor.decrypt(source);
  }
  
  public static FileChannel createOutputChannel(Path outputPath) {
    FileChannel channel = null;
    try {
      if (Files.exists(outputPath, LinkOption.NOFOLLOW_LINKS)) {
        Files.delete(outputPath);
      }
      channel = FileChannel.open(outputPath, StandardOpenOption.CREATE_NEW,
          StandardOpenOption.WRITE, StandardOpenOption.DSYNC);
      channel.lock();
    } catch (IOException e) {
      System.out.println("Failed to open output channel! --> " + e.getMessage());
    }
    return channel;
  }
  
}
