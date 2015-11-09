timeFormat = (length) ->
  seconds = length % 60
  minutes = length / 60
  '' + (if minutes < 10 then '0' + minutes else minutes) + 
  	':' + (if seconds < 10 then '0' + seconds else seconds)

console.log timeFormat 100

$videoContainer = $('.video-container')

$video = $videoContainer.find '#player' 
video = $video.get 0

$video.on 'loadstart', ->
  console.log 'Load started'

$video.on 'loadend', ->
  console.log 'Load ended'

$video.on 'lodeddata', ->
  console.log 'Seeking'

$video.on 'waiting', ->
  console.log 'Waiting'

$video.on 'progress', ->
  console.log 'progress'
  
$video.on 'timeupdate', ->
  $video.find('.current-time').html timeFormat video.played.end 0
  console.log 'time updated'
  
$videoContainer.on
  'click': ->
    video = ($(this).find 'video').get 0
    if video.paused then video.play() else video.pause()
    $('.play-pause').toggleClass? 'hidden'