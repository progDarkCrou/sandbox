(function() {
  var $progress, $progressLine, $video, $videoContainer, onClick, setProgressPosition, timeFormat, video;
  timeFormat = function(length) {
    var minutes, seconds;
    seconds = Math.floor(length % 60);
    minutes = Math.floor(length / 60);
    return '' + (minutes < 10 ? '0' + minutes : minutes) + ':' + (seconds < 10 ? '0' + seconds : seconds);
  };
  $videoContainer = $('.video-container');
  $video = $videoContainer.find('#player');
  video = $video.get(0);
  $progress = $videoContainer.find('.progress-bar');
  $progressLine = $progress.find('.progress-line');
  $video.on('loadstart', function() {
    return console.log('Load started');
  });
  $video.on('canplay', function() {
    $videoContainer.find('.duration').html(timeFormat(video.duration));
    return console.log('Video can play');
  });
  $video.on('lodeddata', function() {
    return console.log('Seeking');
  });
  $video.on('waiting', function() {
    return console.log('Waiting');
  });
  $video.on('progress', function() {
    return console.log('progress');
  });
  $video.on('timeupdate', function() {
    return $videoContainer.find('.current-time').html(timeFormat(video.played.end(0)));
  });
  $video.on('ended', function() {
    return $('.play-pause').addClass('hidden');
  });
  setProgressPosition = function(e) {
    var nextTimePos;
    nextTimePos = (e.offsetX / video.width) * video.duration || 1;
    $progressLine.width(e.offsetX);
    return video.currentTime = nextTimePos;
  };
  $progress.on({
    'click': setProgressPosition
  });
  $progress.on({
    'drag': setProgressPosition
  });
  onClick = function() {
    if (video.paused) {
      video.play();
    } else {
      video.pause();
    }
    return $('.play-pause').toggleClass('hidden');
  };
  $('.play-pause').on({
    'click': onClick
  });
  return $($video).on({
    'click': onClick
  });
})();
