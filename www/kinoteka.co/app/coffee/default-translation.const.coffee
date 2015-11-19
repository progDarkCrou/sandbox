((angular) ->
  angular.module('application').constant 'translations', {
    eng:
      nav:
        home:
          name: 'home'
          title: 'Home'
        films:
          name: 'film'
          title: 'Films'
        shows:
          name: 'shows'
          title: 'Shows'
        about:
          name: 'about'
          title: 'About'
    rus:
      nav:
        home:
          name: 'home'
          title: 'Главная'
        films:
          name: 'film'
          title: 'Фильмы'
        shows:
          name: 'shows'
          title: 'Сериалы'
        about:
          name: 'about'
          title: 'Про нас'
  })(angular)