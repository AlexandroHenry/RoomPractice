// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.1" apply false

//    Dao suspend 관련 문제 해결 포인트 : 버전이 어떤게 또 가능한지는 아직 모름
    id("org.jetbrains.kotlin.android") version "1.6.0" apply false
}