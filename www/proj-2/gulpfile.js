//Configs
var appSrcPath = './app';
var appCssSrcPath = appSrcPath + '/css/';
var appJsSrcPath = appSrcPath + '/js/';
var devDestSrc = './dest';
var tmpSrc = './.tmp';
var tmpCssSrc = tmpSrc + '/css';
var tmpJsSrc = tmpSrc + '/js';


//Requires
var gulp = require('gulp');
var serve = require('gulp-webserver');
var watch = require('gulp-watch');
var inject = require('gulp-inject');
var bowerFiles = require('main-bower-files');
var concat = require('gulp-concat');
var del = require('del');
var concatCss = require('gulp-concat-css');

gulp.task('serve', ['watch'], function() {
  gulp.src(devDestSrc)
    .pipe(serve({
      host: '0.0.0.0',
      port: 9000,
      livereload: true,
      directoryListen: true
    }))
});

gulp.task('clean', function() {
  del(['./.tmp/**', './dest/**'], {
    force: true
  });
});

gulp.task('watch', function() {
  gulp.watch('./app/**/*', ['package']);
});

gulp.task('bower-css', function() {
  gulp.src(bowerCss()).pipe(concat('vendor.css')).pipe(gulp.dest('./.tmp/vendor/css'));
});

gulp.task('css', function() {
  gulp.src('./app/css/**/*.css').pipe(concat('main.css')).pipe(gulp.dest('./.tmp/css'));
});

gulp.task('bower-js', function() {
  gulp.src(bowerJs()).pipe(concat('vendor.js')).pipe(gulp.dest('./.tmp/vendor/js'));
});

gulp.task('js', function() {
  gulp.src('./app/js/**/*.js').pipe(concat('main.js')).pipe(gulp.dest('./.tmp/js'));
});

gulp.task('package', ['bower-css', 'bower-js', 'js', 'css'], function() {
  var target = gulp.src('./app/index.html');
  var sources = gulp.src(['./.tmp/vendor/*.js', './.tmp/css/*.css'], {
    read: false,
    base: './.tmp'
  });

  return target.pipe(inject(sources))
    .pipe(gulp.dest('./.tmp'));
});

function bowerCss() {
  return bowerFiles().filter(function(e) {
    return e.indexOf('.css') !== -1;
  });
}

function bowerJs() {
  return bowerFiles().filter(function(e) {
    return e.indexOf('.js') !== -1;
  });
}
