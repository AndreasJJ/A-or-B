import { FetchError } from './FetchError';

export async function fetcher(url: string, token?: string): Promise<any> {
  const response = await fetch(url, {
    method: 'GET',
    mode: 'cors',
    cache: 'no-cache',
    credentials: 'include',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
    redirect: 'follow',
    referrerPolicy: 'no-referrer',
  });

  // If response is OK then extract result and update the state if it's not null
  if (response.ok && response.status === 200) {
    return response.json();
  }

  // Throw appropriate error if fetch was not successful
  if (response.status === 500) {
    throw new FetchError('Internal Server Error', 500);
  } else if (response.status === 401) {
    throw new FetchError('Invalid token', 401);
  } else if (response.status === 403) {
    throw new FetchError('Forbidden, insufficient permissions', 403);
  } else if (response.status === 404) {
    throw new FetchError('Data not found', 404);
  } else {
    throw new Error('Unexpected error');
  }
}
