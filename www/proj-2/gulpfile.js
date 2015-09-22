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
var gulpSync = require('gulp-sync')(gulp);

gulp.task('serve', ['watch'], function() {
  gulp.src('.tmp/')
    .pipe(serve({
      host: '0.0.0.0',
      port: 9000,
      livereload: true,
      directoryListen: true
    }));
});

gulp.task('clean', function(cb) {
  var d = del(['.tmp'], {
    force: true
  });
  d.then(function () {
  	console.log('End of the cleaning..');
  	cb();
  });
});

gulp.task('watch', ['package'], function() {
  gulp.watch('./app/**/*', ['package']);
});

gulp.task('bower-css', function(cb) {
  gulp.src(bowerCss()).pipe(concat('vendor.css')).pipe(gulp.dest('.tmp/vendor/css')).on('end', cb);
});

gulp.task('css', function(cb) {
  gulp.src('./app/css/**/*.css').pipe(concat('main.css')).pipe(gulp.dest('.tmp/css')).on('end', cb);
});

gulp.task('bower-js', function(cb) {
  gulp.src(bowerJs()).pipe(concat('vendor.js')).pipe(gulp.dest('.tmp/vendor/js')).on('end', cb);
});

gulp.task('js', function(cb) {
  gulp.src('./app/js/**/*.js').pipe(concat('main.js')).pipe(gulp.dest('.tmp/js')).on('end', cb);
});

gulp.task('html', function(cb) {
  gulp.src('app/**/*.html').pipe(gulp.dest('./.tmp')).on('end', cb);
});

gulp.task('package', gulpSync.sync(['clean', 'js', 'css', 'bower-css', 'bower-js', 'html', 'wiredep']));

gulp.task('wiredep', function(cb) {
  var target = gulp.src('app/index.html');
  var sourcesVendor = gulp.src(['.tmp/vendor/**/*.js', '.tmp/vendor/**/*.css']);
  var sources = gulp.src(['.tmp/js/**/*.js', '.tmp/css/**/*.css']);

  target.pipe(inject(sourcesVendor, {
      ignorePath: '.tmp',
      name: 'vendor'
    }))
    .pipe(gulp.dest('.tmp'))
    .pipe(inject(sources, {
      ignorePath: '.tmp'
    }))
    .pipe(gulp.dest('.tmp'))
    .on('end', cb);
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
