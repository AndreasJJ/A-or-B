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
export const RoundGalleryVideo: React.FC<RoundGalleryVideoProps> = ({ title, link }) => {
  let url = new URL(link);
  let id = url.searchParams.get("v");
  let newUrl = `https://www.youtube.com/embed/${id}`;

  return (
    <Video>
      <iframe
        width="560"
        height="315"
        src={newUrl}
        frameBorder="0"
        allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
        allowFullScreen
      >
      </iframe>
    </Video>
  );
};
