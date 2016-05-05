/**
 * Created by avorona on 28.01.16.
 */

var gulp = require('gulp');
var inject = require('gulp-inject');
var tsc = require('gulp-tsc');
var rjs = require('gulp-requirejs-optimize');
var uglify = require('gulp-uglify');

var sync = require('gulp-sync')(gulp);
var serve = require('gulp-webserver');

var del = require('del');

var depsRoot = 'bower_components';

var deps = [
  'require.js',
  'dist/jquery.js',
  'angular.js'
];

gulp.task('tsc', function () {
  return gulp.src(['src/main/js/**/*.ts', 'typings/**/*.d.ts']).pipe(tsc({
    module: "system",
    target: "es5",
    sourceMap: false,
    rootDir: "src/main/js/",
    isolatedModules: true
  })).pipe(gulp.dest('.tmp/compile/'));
});

gulp.task('rjs', ['tsc'], function () {
  return gulp.src('.tmp/compile/**/*.js').pipe(rjs({
    paths: {
      jquery: "empty:",
      angular: "empty:"
    },
    optimize: 'uglify'
  })).pipe(gulp.dest('.tmp/build/'));
});

gulp.task('js-compress', ['tsc'], function () {
  return gulp.src(['.tmp/compile/**/*.js']).pipe(uglify()).pipe(gulp.dest('build/js/'));
});

gulp.task('js-deps', function () {
  gulp.src(deps.map(function (e) {
    return depsRoot + '/**/' + e;
  })).pipe(gulp.dest('build/deps/'));
});

gulp.task('js', ['js-compress', 'js-deps'], function () {
  return gulp.src('.tmp/build/**.js').pipe(gulp.dest('build/js/'));
});

gulp.task('html', function () {
  return gulp.src('src/main/**.html').pipe(gulp.dest('.tmp/'));
});

gulp.task('build-js', ['js'], function (cb) {
  var depsJs = gulp.src(deps.map(function (e) {
    return 'build/deps/**/' + e;
  }), {read: false});
  gulp.src('.tmp/index.html').pipe(inject(depsJs, {
    ignorePath: 'build',
    name: 'deps'
  })).pipe(gulp.dest('build/')).on('end', function () {
    var js = gulp.src(['build/js/index.js', 'build/js/**/*.js'], {read: false});
    gulp.src(['build/index.html']).pipe(inject(js, {
      ignorePath: 'build'
    })).pipe(gulp.dest('build/')).on('end', cb);
  });
});

gulp.task('clean', function () {
  del.sync(['.tmp/**', 'build/**']);
});

gulp.task('build', sync.sync(['clean', 'html', 'build-js']), function () {
});

gulp.task('watch', ['build'], function () {
  return gulp.watch('src/main/**/*', ['build']);
});

gulp.task('serve', ['watch'], function () {
  gulp.src('build/').pipe(serve({
    livereload: true,
    port: 3000,
    open: true
  }));
});