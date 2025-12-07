const formatDate = (dateStr: string) => {
  const [year, month, day] = dateStr.split("-");
  return `${day}/${month}/${year}`;
};

export default formatDate