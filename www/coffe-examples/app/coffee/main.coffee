(->
  timeFormat = (length) ->
    seconds = Math.floor (length % 60)
    minutes = Math.floor (length / 60)
    '' + (if minutes < 10 then '0' + minutes else minutes) + 
      ':' + (if seconds < 10 then '0' + seconds else seconds)

  $videoContainer = $('.video-container')
  $video = $videoContainer.find '#player' 
  video = $video.get 0
  $progress = $videoContainer.find '.progress-bar'
  $progressLine = $progress.find '.progress-line'

  $video.on 'loadstart', ->
    console.log 'Load started'

  $video.on 'canplay', ->
    $videoContainer.find('.duration').html timeFormat video.duration
    console.log 'Video can play'

  $video.on 'lodeddata', ->
    console.log 'Seeking'

  $video.on 'waiting', ->
    console.log 'Waiting'

  $video.on 'progress', ->
    console.log 'progress'

  $video.on 'timeupdate', ->
    $videoContainer.find('.current-time').html(timeFormat(video.played.end(0)))

  $video.on 'ended', ->
    $('.play-pause').addClass 'hidden'

  setProgressPosition = (e)->
    nextTimePos = (e.offsetX / video.width) * video.duration or 1
    $progressLine.width e.offsetX
    video.currentTime = nextTimePos

  $progress.on 'click': setProgressPosition
  $progress.on 'drag': setProgressPosition

  onClick = ->
    if video.paused then video.play() else video.pause()
    $('.play-pause').toggleClass 'hidden'

  $('.play-pause').on 'click': onClick 
  $($video).on
    'click': onClick

)()