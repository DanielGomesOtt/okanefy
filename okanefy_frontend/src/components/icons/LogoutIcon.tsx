import React from "react";

interface IconProps extends React.SVGProps<SVGSVGElement> {
  size?: number;
  height?: number;
  width?: number;
  fill?: string;
}

export const LogoutIcon: React.FC<IconProps> = ({
  fill = "currentColor",
  size,
  height,
  width,
  ...props
}) => {
  return (
    <svg
      data-name="Iconly/Curved/Logout"
      height={size || height || 24}
      viewBox="0 0 24 24"
      width={size || width || 24}
      xmlns="http://www.w3.org/2000/svg"
      {...props}
    >
      <g
        fill="none"
        stroke={fill}
        strokeLinecap="round"
        strokeLinejoin="round"
        strokeMiterlimit={10}
        strokeWidth={1.5}
      >
        <path
          d="M15.43 7.96V6.56c0-1.89-1.47-3.42-3.28-3.42H6.72C4.92 3.14 3.45 4.67 3.45 6.56v10.88c0 1.89 1.47 3.42 3.27 3.42h5.43c1.81 0 3.28-1.53 3.28-3.42v-1.42"
        />
        <path d="M20.55 12h-8.73" />
        <path d="M17.84 9.29L20.55 12l-2.71 2.71" />
      </g>
    </svg>
  );
};
