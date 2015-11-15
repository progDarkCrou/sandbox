((angular) ->
  angular.module('application').constant 'translations', {
    eng:
      nav:
        home:
          name: 'home'
          title: 'Home'
        films:
          name: 'films'
          title: 'Films'
        shows:
          name: 'shows'
          title: 'Shows'
        about:
          name: 'about'
          title: 'About'
  })(angular)