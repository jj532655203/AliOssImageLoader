package com.fronttcapital.imageloader;

public interface Constants {
    long DISK_LRU_CACHE_SIZE = 1024 * 1024 * 1000;
    String DISK_LRU_CACHE_DIR = "disk_lru_cache_dir";

    interface AliOssErrorMsg {
        String NO_SUCH_KEY = "NoSuchKey";
    }
}
