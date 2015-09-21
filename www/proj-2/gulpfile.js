//Configs
var appSrcPath = './app';
var appCssSrcPath = appSrcPath + '/css/';
var appJsSrcPath = appSrcPath + '/js/';

//Requires
var gulp = require('gulp');
var serve = require('gulp-webserver');
var watch = require('gulp-watch');
var inject = require('gulp-inject');
var bowerFiles = require('main-bower-files');


gulp.task('serve', ['build'], function() {
  gulp.src('./dest')
    .pipe(serve({
      host: '0.0.0.0',
      port: 9000,
      livereload: true,
      directoryListen: true
    }))
});

gulp.task('css', function() {
  gulp.src(appSrcPath + '/**/*.css')
    .pipe(watch(appSrcPath + '/**/*.css'))
    .pipe(gulp.dest('./dest'))
});

gulp.task('bower', function() {
  gulp.src(bowerFiles())
    .pipe(gulp.dest('./dest/vendor'));
});

gulp.task('build', ['bower', 'css'], function() {
  gulp.src(appSrcPath + '/**/*.html')
    .pipe(watch(appSrcPath + '/**/*.html'))
    .pipe(inject(gulp.src(['./dest/vendor/*.css', './dest/vendor/*.less','./dest/vendor/*.js']), {
      transform: function(filepath) {
        if (filepath.slice(-5) === '.less') {
        	return '<link rel="stylesheet" href="' + filepath.slice(5) + '"/>';
        }
        arguments[0] = filepath.slice(5);
        return inject.transform.apply(inject.transform, arguments);
      }
    }))
    .pipe(gulp.dest('./dest'));
});
