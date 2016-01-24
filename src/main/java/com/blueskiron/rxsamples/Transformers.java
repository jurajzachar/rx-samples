package com.blueskiron.rxsamples;

import java.io.IOException;
import java.util.zip.DataFormatException;

import rx.Observable;
import rx.Observable.Transformer;

public class Transformers {

  /**
   * @param source
   * @return
   */
  public final static Transformer<byte[], byte[]> encrypt() {
    return new Transformer<byte[], byte[]>() {
      @Override
      public Observable<byte[]> call(Observable<byte[]> source) {
        return source.map(binArr -> Utils.encryptor.encrypt(binArr));
      }
    };
  }

  /**
   * @param source
   * @return
   */
  public final static Transformer<byte[], byte[]> decrypt() {
    return new Transformer<byte[], byte[]>() {
      @Override
      public Observable<byte[]> call(Observable<byte[]> source) {
        return source.map(binArr -> Utils.encryptor.decrypt(binArr));
      }
    };
  }

  /**
   * @param source
   * @return
   */
  public final static Transformer<byte[], byte[]> compress() {
    return new Transformer<byte[], byte[]>() {
      @Override
      public Observable<byte[]> call(Observable<byte[]> source) {
        return source.map(binArr -> {
          try {
            return Utils.compress((byte[]) binArr);
          } catch (IOException e) {
            System.out.println("Eeek! Compression failed -> " + e.getMessage());
            return (byte[]) binArr;
          }
        });
      }
    };
  }

  /**
   * @return
   */
  public final static Transformer<byte[], byte[]> decompress() {
    return new Transformer<byte[], byte[]>() {
      @Override
      public Observable<byte[]> call(Observable<byte[]> source) {
        return source.map(binArr -> {
          try {
            return Utils.decompress((byte[]) binArr);
          } catch (DataFormatException | IOException e) {
            System.out.println("Eeek! Decompression failed -> " + e.getMessage());
            return (byte[]) binArr;
          }
        });
      }
    };
  }

}
