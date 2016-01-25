package com.blueskiron.rxsamples.launchers;

import static com.blueskiron.rxsamples.files.Utils.DEFAULT_MAX_BYTES_PER_EMISSION;
import static com.blueskiron.rxsamples.files.Utils.createOutputChannel;
import static com.blueskiron.rxsamples.files.Utils.decompress;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.DataFormatException;

import rx.Observable;
import rx.Observer;

import com.blueskiron.rxsamples.files.Utils;

public class Observers {

  public static Observer<List<byte>> createLocalObserver(Observable<List<byte[]>> observable, Path outputPath) {
    // consumer reverses the whole process
    final FileChannel outputChannel = createOutputChannel(outputPath);
    final ByteBuffer buf = ByteBuffer.allocate(DEFAULT_MAX_BYTES_PER_EMISSION);
    return null;
  }
}
