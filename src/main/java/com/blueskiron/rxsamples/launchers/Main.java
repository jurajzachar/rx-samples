package com.blueskiron.rxsamples.launchers;

import static com.blueskiron.rxsamples.files.Transformers.compress;
import static com.blueskiron.rxsamples.files.Transformers.encrypt;
import static com.blueskiron.rxsamples.files.Utils.DEFAULT_MAX_BYTES_PER_EMISSION;
import static com.blueskiron.rxsamples.files.Utils.createOutputChannel;
import static com.blueskiron.rxsamples.files.Utils.decompress;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.DataFormatException;

import com.blueskiron.rxsamples.files.FileObservables;
import com.blueskiron.rxsamples.files.Utils;

import rx.Observable;
import rx.Observer;

public class Main {

  public static void main(String[] args) throws IOException {

    // stream file from its beginning with defined chunk size
    Observable<List<byte[]>> observable = FileObservables
        .fromFile(Paths.get("src/main/resources", "sample_file_10MB.mkv"), 0, Utils.DEFAULT_MAX_BYTES_PER_EMISSION)
        .compose(compress()) // compress and encrypt every chunk
        .compose(encrypt())
        .buffer(100); //instead of emitting one by one, bundle up into size of 100
  
    
    //subscribe locally
    createLocalObserver(observable, Paths.get("/tmp", "observed_file.mkv"));
    
    //subscribe via netty server
    
    
  }
}
