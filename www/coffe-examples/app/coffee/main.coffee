timeFormat = (length) ->
	seconds = Math.floor (length % 60)
	minutes = Math.floor (length / 60)
	'' + (if minutes < 10 then '0' + minutes else minutes) + 
  	':' + (if seconds < 10 then '0' + seconds else seconds)

console.log timeFormat 100

$videoContainer = $('.video-container')

$video = $videoContainer.find '#player' 
video = $video.get 0


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
  $videoContainer.find('.current-time').html(timeFormat(video.played.end 0))
  
$videoContainer.on
  'click': ->
    video = ($(this).find 'video').get 0
    if video.paused then video.play() else video.pause()
    $('.play-pause').toggleClass? 'hidden'