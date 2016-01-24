package com.blueskiron.rxsamples;

import static com.blueskiron.rxsamples.Transformers.compress;
import static com.blueskiron.rxsamples.Transformers.encrypt;
import static com.blueskiron.rxsamples.Utils.DEFAULT_MAX_BYTES_PER_EMISSION;
import static com.blueskiron.rxsamples.Utils.encryptor;
import static com.blueskiron.rxsamples.Utils.createOutputChannel;
import static com.blueskiron.rxsamples.Utils.decompress;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.DataFormatException;

import rx.Observable;
import rx.Observer;

public class Main {

  public static void main(String[] args) throws IOException {

    // stream from the beginning with sample time of 250 ms
    Observable<byte[]> producer = FileObservables
        .streamFile(Paths.get("src/main/resources", "sample_file.mkv"), 0, 250)
        .compose(compress())
        .compose(encrypt());
  

    // consumer reverses the whole process
    final Path outputPath = Paths.get("/tmp", "received_file.mkv");
    final FileChannel outputChannel = createOutputChannel(outputPath);
    final ByteBuffer buf = ByteBuffer.allocate(DEFAULT_MAX_BYTES_PER_EMISSION);

    producer.subscribe(new Observer<byte[]>() {
      @Override
      public void onNext(byte[] payload) {
        try {
          byte[] unwrapped = decompress(encryptor.decrypt(payload));
          System.out.println(unwrapped);
          buf.clear();
          buf.put(unwrapped);
          buf.flip();
          outputChannel.write(buf, outputChannel.size());
        } catch (IOException | DataFormatException e) {
          System.out.println("Eeek! " + e.getMessage());
        }
      }

      @Override
      public void onCompleted() {
        System.out.println("streaming finished, will close output channel");
        try {
          outputChannel.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onError(Throwable t) {
        System.out.println("an error occured while receiving the stream, will try to recover...");
        t.printStackTrace();
        // TODO
      }
      
    });
    
  }
}
