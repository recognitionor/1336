package com.ham.onettsix.ad

interface RewardedVideoListener<T> {
    fun onAdLoaded(rv: T)
    fun onAdFailedToLoad(error: String) {
        System.err.println("~~~~~~~~~~")
    }
}