package com.blueskiron.rxsamples;

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
   * Streams a file in chunks via Observable
   * 
   * @param filePath
   * @param startPosition
   * @param sampleTimeMs
   * @param chunkSize
   * @return
   * @throws IOException
   */
  public final static Observable<byte[]> streamFile(Path filePath, long startPosition,
      long sampleTimeMs, int chunkSize) {

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
            subscriber.onNext(buffer.array());
          }
          subscriber.onCompleted(); // tell subscriber we are done
          // reading the file
          buffer.clear(); // clear the buffer
          channel.close(); // close the data channel
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

      }
    });
    //.sample(sampleTimeMs, TimeUnit.MILLISECONDS);
    
  }

  public final static Observable<byte[]> streamFile(Path filePath, long startPosition,
      long sampleTimeMs) throws IOException {
    return streamFile(filePath, startPosition, sampleTimeMs, Utils.DEFAULT_MAX_BYTES_PER_EMISSION);
  }
}