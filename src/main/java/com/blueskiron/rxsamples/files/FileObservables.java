package com.blueskiron.rxsamples.files;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

public class FileObservables {

  /**
   * Streams a file in chunks as an Observable
   * 
   * @param filePath
   * @param startPosition
   * @param sampleTimeMs
   * @param chunkSize
   * @return
   * @throws IOException
   */
  public final static Observable<byte[]> fromFile(Path filePath, long startPosition, int chunkSize) {

    return Observable.create(new OnSubscribe<byte[]>() {

      @Override
      public void call(Subscriber<? super byte[]> subscriber) {
        try {
          
          final FileChannel channel = FileChannel.open(filePath, StandardOpenOption.READ);
          final ByteBuffer buffer = ByteBuffer.allocate(chunkSize);

          if (subscriber.isUnsubscribed()) {
            return;
          }
          while (channel.read(buffer) > 0) {
            buffer.flip();
            byte[] payload = buffer.array();
            System.out.println("emitted: " + java.util.Arrays.toString(payload));
            subscriber.onNext(payload);
          }
          subscriber.onCompleted(); // tell subscriber we are done
          // reading the file
          buffer.clear(); // clear the buffer
          channel.close(); // close the data channel
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
    
    
  }

  public final static Observable<byte[]> fromFile(Path filePath, long startPosition) throws IOException {
    return fromFile(filePath, startPosition, Utils.DEFAULT_MAX_BYTES_PER_EMISSION);
  }
}