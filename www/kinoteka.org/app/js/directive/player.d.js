(function (angular) {
    angular
        .module('directives')
        .directive('player', ['$sce', function function_name($sce) {
            return {
                scope: {},
                link: function (scope, el, attrs) {
                    scope.player = {};

                    var $playback = el.find('.playback');

                    var playback = $playback.get(0);

                    $playback.on('click', function () {
                        playback.paused ? playback.play() : playback.pause();
                    });

                    var player = scope.player;

                    player.play = function () {

                    };

                    player.pause = function () {

                    };

                    player.stop = function () {

                    };

                    player.playEpisode = function (season, episode) {
                        playback.pause();
                        player.currentSeason = season;
                        player.currentEpisode = episode;
                        playback.load();
                        playback.play();
                    };

                    player.seasons = [{
                        name: 'season 1',
                        episodes: [{
                            name: 'Hobbit undefined',
                            url: $sce.trustAsResourceUrl('https://11-lvl3-pdl.vimeocdn.com/01/3937/2/69686672/175207471.mp4?expires=1447015733&token=0c7c567398d2b7d13a660')
                        }, {
                            name: 'Something video',
                            url: $sce.trustAsResourceUrl('https://09-lvl3-pdl.vimeocdn.com/01/3933/2/69665635/175207482.mp4?expires=1447016913&token=0a431e77c748ef3b69dcc')
                        }]
                    }];

                    player.currentSeason = player.seasons[0];
                    player.currentEpisode = player.currentSeason.episodes[0];
                },
                templateUrl: 'templates/player/player.tmp.html'
            };
        }]);
})(angular);
