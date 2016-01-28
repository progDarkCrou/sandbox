/**
 * Created by avorona on 28.01.16.
 */

var gulp = require('gulp');
var inject = require('gulp-inject');
var tsc = require('gulp-tsc');
var rjs = require('gulp-requirejs-optimize');

gulp.task('tsc', function () {
  return gulp.src(['src/main/js/**.ts', 'typings/**.d.ts']).pipe(tsc({
    module: "amd",
    target: "es5",
    sourceMap: false,
    rootDir: "src/main/js/"
  })).pipe(gulp.dest('.tmp/compile/'));
});

gulp.task('rjs', ['tsc'], function () {
  return gulp.src('.tmp/compile/**.js').pipe(rjs({
    paths: {
      jquery: "empty:",
      angular: "empty:"
    }
  })).pipe(gulp.dest('.tmp/build/'));
});

gulp.task('js', ['rjs'], function () {
  return gulp.src('.tmp/build/**.js').pipe(gulp.dest('dest/js/'));
});

gulp.task('html', function () {
  return gulp.src('src/main/**.html').pipe(gulp.dest('.tmp/'));
});

gulp.task('build', ['js', 'html'], function () {
  var injectJs = gulp.src('dest/js/**.js', {read: false});
  return gulp.src('.tmp/index.html').pipe(inject(injectJs)).pipe(gulp.dest('dest/'));
});