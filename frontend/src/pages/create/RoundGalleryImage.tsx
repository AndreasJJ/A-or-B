import React from 'react';
import styled from 'styled-components';

const Image = styled.div`
  display: block;
  max-width: 560px;
  max-height: 560px;
  width: auto;
  height: auto;
`;

const Img = styled.img`
  width: 100%;
  object-fit: cover;
`;

interface RoundGalleryImageProps {
    title: string;
    link: string;
}

/**
 * Component for
 */
export const RoundGalleryImage: React.FC<RoundGalleryImageProps> = ({ title, link }) => {
  return (
    <Image>
      <Img src={link} />
    </Image>
  );
};