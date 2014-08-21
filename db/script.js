use playSite

db.dropDatabase()

db.createCollection('users')

db.users.insert({
    name: {first: 'John', last: 'Doe'},
    displayName: 'John',
    email: 'john.doe@me.com',
    avatarUrl: 'http://icons.iconarchive.com/icons/oxygen-icons.org/oxygen/128/Places-user-identity-icon.png',
    website: 'http://www.theejohndoe.com/',
    shortBio: ''
})

db.users.insert({
    name: {first: 'John', last: 'Doe'},
    displayName: 'John',
    email: 'john.doe@me.com',
    avatarUrl: 'http://icons.iconarchive.com/icons/custom-icon-design/pretty-office-2/128/man-icon.png',
    website: 'http://www.janedoe.org/',
    shortBio: ''
})