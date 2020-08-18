import Keycloak from 'keycloak-js';

// Setup Keycloak instance as needed
// Pass initialization options as required or leave blank to load from 'keycloak.json'
const keycloak = new Keycloak({
    realm: 'Frontend',
    url: '/auth/',
    resource: 'react',
    clientId: 'react',
    publicClient: true,
    confidentialPort: 0,
});

export default keycloak;