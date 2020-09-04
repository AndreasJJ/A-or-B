import React from 'react';
import styled from 'styled-components';

const Video = styled.div`

`;

interface RoundGalleryVideoProps {
    title: string;
    link: string;
}

/**
 * Component for
 */
export const RoundGalleryVideo: React.FC<RoundGalleryVideoProps> = ({ link }) => {
  const url = new URL(link);
  const id = url.searchParams.get('v');
  const newUrl = `https://www.youtube.com/embed/${id}`;

  return (
    <Video>
      <iframe
        title="youtube video"
        width="560"
        height="315"
        src={newUrl}
        frameBorder="0"
        allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
        allowFullScreen
      />
    </Video>
  );
};
