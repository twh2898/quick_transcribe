@echo off

SETLOCAL

set INPUT=%1
set OUTPUT=%2

echo "Converting %INPUT% to %OUTPUT%"
ffmpeg -i %INPUT% -acodec pcm_s16le -ac 1 -ar 8000 %OUTPUT%

echo "Finished with %ERRORLEVEL%"

