# quick_transcribe

An implementation of the [sphinx4](https://github.com/cmusphinx/sphinx4) library to transcribe audio files.
The code is based on the sphinx [demo](https://cmusphinx.github.io/wiki/tutorialsphinx4/).

## Using a pre-build release

Download the latest [release](https://github.com/twh2898/quick_transcribe/releases) and extract the files.

The release includes the following files:

- `audio_convert.bat` a script to convert audio files in to the sphinx required format using ffmpeg
- `audio_convert.sh` a script to convert audio files into the sphinx required format using ffmpeg
- `audio_convert_8k.bat` a script to convert audio files in to the 8kHz format using ffmpeg (use only if original sample rate is < 16kHz)
- `audio_convert_8k.sh` a script to convert audio files into the 8kHz required format using ffmpeg (use only if original sample rate is < 16kHz)
- `run_app.bat` - run the application, for windows users
- `run_app.sh` - run the application, for \*nix and macos users

## Building from source

### Download

Download the source from a [release](https://github.com/twh2898/quick_transcribe/releases) or clone the repository.
Using [gradle](https://docs.gradle.org/current/userguide/getting_started.html)

```sh
git clone https://github.com/twh2898/quick_transcribe.git
cd quick_transcribe
```

### Building with gradle

```sh
gradle build
```

And to run the project

```sh
gradle run
```

### Build using a gradle wrapper

```sh
gradle wrapper
./gradlew build
```

For windows replace `./gradlew` with `./gradlew.bat`

```batch
./gradlew.bat build
```

And to run the project

```sh
./gradlew run
```

or

```batch
./gradlew.bat run
```

## Licence

quick\_transcribe uses the [MIT](LICENCE) licence 

