(function(angular) {

	var directives = angular.module('directives', []);

    var application = angular.module('app', ['directives']);

    application.run(function() {
        $('.main-container').on('click', function() {
            console.log('Clicker on the .main-container');
        });
    });
})(angular);
