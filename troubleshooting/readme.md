# Troubleshooting Errors in Scala Native

## Missing Definitions While Linking

The following error might occur during the linking phase when compiling Scala Native code:

```
[error] Found 1 missing definition while linking
[error] Not found Member(Top(maf.lattice.interfaces.RealLattice))
[error] Undefined definitions found in reachability phase
[error] (mafNative / Compile / nativeLink) Undefined definitions found in reachability phase
```

There are multiple reasons for this error:

### 1.      Unreachable Code

As mentioned before, Scala Native can reach, and thus compile, all the code that is in the `native` folder, and the `shared` folder (in case of cross-platform Scala projects). If you try to import or run code from other folders, such as `jvm` or `js`, the code will not compile and you will probably get an unreachability error during the linking phase. If you happen to have this problem, the only solution would be to refactor your code and remove all imports that are not reachable for Scala Native

### 2.   Problems With Libraries

Another potential cause of the unreachability error is that libraries (whether these are external libraries of Javalib libraries[^1]) are not fully implemented and contain some dummy stabbing implementation. Solving this error is unfortunately harder. You will have to look for an alternative implementation for that libary or maybe another library that could offer similar functionality. 

### 3.      All Code Is Provided and Reachable?

If all the above-mentioned causes are not present and the unreachability error still occurs, you can try to clean the generated files using `sbt clean` if sbt is used.


[^1] <https://github.com/scala-native/scala-native/issues/3215>