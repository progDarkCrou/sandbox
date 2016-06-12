//Configs
var bowerFile = 'bower.json';

var appSrc = 'app';
var appCssSrc = appSrc + '/css/';
var appJsSrc = appSrc + '/js/';
var appCoffeeSrc = appSrc + '/coffee/';
var appLessSrc = appSrc + '/less/';
var devDestSrc = 'dest';
var tmp = '.tmp';
var tmpCss = tmp + '/css';
var tmpJs = tmp + '/js';
var tmpJsVendor = tmp + '/vendor/js';
var tmpCssVendor = tmp + '/vendor/css';
var tmpCoffeeCompiled = tmpJs + '/coffee-compiled'
var tmpLessCompiled = tmpCss + '/less-compiled'

//Requires
var gulp = require('gulp');
var serve = require('gulp-webserver');
var watch = require('gulp-watch');
var inject = require('gulp-inject');
var concat = require('gulp-concat');
var gulpSync = require('gulp-sync')(gulp);
var coffee = require('gulp-coffee');
var bowerFiles = require('main-bower-files');
var del = require('del');
var less = require('gulp-less');

//Utils
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

function setUpWatch(watchForArray, watchTasksArray, cb) {
  var watcher = watch(watchForArray, function() {
    watchTasksArray.forEach(function(t) {
      gulp.start(t);
    })
  });
  watcher.on('change', function() {
    console.log('[Watch] - Some files changed');
    console.log('[Watch] - going to do this tasks: [' + watchTasksArray.join(', ') + ']');
  })
}

//Tasks
gulp.task('serve', ['tmp'], function() {
  setUpWatch([appSrc + '/**/*'], ['tmp']);

  gulp.src(tmp)
    .pipe(serve({
      host: '0.0.0.0',
      port: 3000,
      livereload: true,
      directoryListen: true
    }));
});

gulp.task('clean', function(cb) {
  var d = del([tmp], {
    force: true
  });
  d.then(function() {
    cb();
  });
});

gulp.task('bower-css', function(cb) {
  gulp.src(bowerCss())
    .pipe(gulp.dest(tmpCssVendor))
    .on('end', cb);
});

gulp.task('less', function(cb) {
  gulp.src(appLessSrc + '/**/*.less')
    .pipe(less())
    .pipe(gulp.dest(tmpLessCompiled))
    .on('end', cb);
});

gulp.task('dev-css', function(cb) {
  gulp.src(appCssSrc + '/**/*.css')
    .pipe(gulp.dest(tmpCss))
    .on('end', cb);
});

gulp.task('css', ['bower-css', 'dev-css', 'less']);

gulp.task('bower-js', function(cb) {
  gulp.src(bowerJs())
    .pipe(gulp.dest(tmpJsVendor))
    .on('end', cb);
});

gulp.task('coffee', function(cb) {
  gulp.src(appCoffeeSrc + '/**/*.coffee')
    .pipe(coffee({
      bare: true
    }))
    .pipe(gulp.dest(tmpCoffeeCompiled))
    .on('end', cb)
});

gulp.task('dev-js', function(cb) {
  gulp.src(appJsSrc + '/**/*.js')
    .pipe(gulp.dest(tmpJsVendor))
    .on('end', cb);
});

gulp.task('js', ['bower-js', 'dev-js', 'coffee']);

gulp.task('html', function(cb) {
  gulp.src(appSrc + '/**/*.html')
    .pipe(gulp.dest(tmp))
    .on('end', cb);
});

gulp.task('json', function(cb) {
  gulp.src(appSrc + '/**/*.json')
    .pipe(gulp.dest(tmp))
    .on('end', cb);
});

gulp.task('wiredep', ['js', 'css', 'html', 'json', 'coffee', 'less'], function(cb) {
  var target = gulp.src(appSrc + '/index.html');
  var sourcesVendor = gulp.src([tmpJsVendor + '/**/*.js', tmpCssVendor + '/**/*.css']);
  var sources = gulp.src([tmpJs + '/**/*.js', tmpCss + '/**/*.css']);

  target.pipe(inject(sourcesVendor, {
      ignorePath: tmp,
      name: 'vendor'
    }))
    .pipe(gulp.dest(tmp))
    .pipe(inject(sources, {
      ignorePath: tmp
    }))
    .pipe(gulp.dest(tmp))
    .on('end', cb);
});

gulp.task('tmp', gulpSync.sync(['clean', 'wiredep']));

gulp.task('watch', function() {
  setUpWatch([appSrc + '/**/*', bowerFile], ['tmp']) });
