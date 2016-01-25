package com.blueskiron.rxsamples.files;

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
        return source.map(binArr -> {
        	byte[] arr = Utils.encrypt(binArr);
        	//System.out.println("encrypted:" + java.util.Arrays.toString(arr));
        	return arr;	
        });
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
          return source.map(binArr -> {
          	byte[] arr = Utils.decrypt(binArr);
          	//System.out.println("decrypted:" + java.util.Arrays.toString(arr));
          	return arr;	
          });
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
            return Utils.compress(binArr);
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
            return Utils.decompress(binArr);
          } catch (DataFormatException | IOException e) {
            System.out.println("Eeek! Decompression failed -> " + e.getMessage());
            return (byte[]) binArr;
          }
        });
      }
    };
  }

}
