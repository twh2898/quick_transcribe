#!/bin/bash

if [ $# -lt 1 ]; then
    echo "Convert audio into a format acceptable by sphinx4"
    echo "Usage: $0 <input> [output]"
    echo "    input: some audio file to be converted"
    echo "    output: destination .wav file (default: test.wav)"
    exit 1
fi

INPUT=$1
if [ $# -gt 1 ]; then
    OUTPUT=$2
else
    OUTPUT=test.wav
fi

echo "Converting $INPUT into $OUTPUT"
ffmpeg -i $INPUT -acodec pcm_s16le -ac 1 -ar 8000 $OUTPUT
RET=$?
echo "Finished with code $RET"

exit $RET

