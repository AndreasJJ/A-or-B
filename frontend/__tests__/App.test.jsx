import React from 'react';
import { render } from '@testing-library/react';
import '@testing-library/jest-dom/extend-expect';

import App from '../src/App';

test('renders Hello World!', () => {
  const { getByText } = render(
    <App />,
  );

  // hello world should be rendered
  const text = getByText('Hello World!');
  expect(text).toBeInTheDocument();
});
