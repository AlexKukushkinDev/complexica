export const navigation = [
  {
    text: 'Home',
    path: '/home',
    icon: 'home'
  },
  {
    text: 'Forecast Weather',
    icon: 'folder',
    items: [
      {
        text: 'Weather',
        path: '/forecast'
      },
      {
        text: 'Itineraries',
        path: '/itineraries'
      }
    ]
  }
];
