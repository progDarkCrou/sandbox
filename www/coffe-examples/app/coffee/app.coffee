angular.module 'player', []

app = angular.module 'application', []

app.controller 'MainCtrl', ['$scope', '$sce', ($scope, $sce) -> 
	$scope.videoSrc = $sce.trustAsResourceUrl 'https://skyfiregcs-a.akamaihd.net/exp=1447101873~acl=%2A%2F437474375.mp4%2A~hmac=9b8f5dfad0a45db555f82d117699ecd508a665051dfabc5c27cc2c586f8332f3/vimeo-prod-skyfire-std-us/01/4006/5/145031134/437474375.mp4'
	$scope.video = $scope.videoSrc
	$scope.$watch 'videoSrc', (newVal) ->
		$scope.video = $sce.trustAsResourceUrl newVal or '' 
	
]