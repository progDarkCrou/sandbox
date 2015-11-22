((angular) ->
  module = angular.module 'application'

  module.controller 'homeCtrl', ['$route', '$scope', ($route, $scope) ->
    $scope.sections = [
      {
        name: 'New on site'
        items: [
          {
            title: 'Iron man'
            year: 2010
            img: 'http://cdn.collider.com/wp-content/uploads/2015/04/iron-man-1-poster.jpg'
          }
          {
            title: 'Iron man 2'
            year: 2011
          }
          {
            title: 'Iron man 3'
            year: 2012
          }
          {
            title: 'Thor'
            year: 2013
            img: 'http://cdn.collider.com/wp-content/uploads/thor-the-dark-world-poster1.jpg'
          }
          {
            title: 'Avengers'
            img: 'http://i1.wp.com/bitcast-a-sm.bitgravity.com/slashfilm/wp/wp-content/images/International-Avengers-Age-of-Ultron-Poster-700x989.jpg?resize=700%2C989'
          }
          {
            title: 'Avengers 2'
            img: 'https://s-media-cache-ak0.pinimg.com/originals/bc/21/53/bc2153bd4587248d645555b249dd7a64.jpg'
          }
          {
            title: 'Hulk'
            img: 'http://cloudfront.posters2prints.com/width/430/bestfit/progressive/quality/75/9344-2.jpg'
          }
        ]
      }
    ]

  ])(angular)