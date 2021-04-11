  
CREATE TABLE  IF NOT EXISTS  api_animalx.usuario(
  id BIGINT PRIMARY KEY  GENERATED ALWAYS AS IDENTITY,
  nome   varchar(40) not null,
  img_login varchar(100),
  email varchar(100) not null,
  senha varchar(100),
  tipo_usuario varchar(10),
  data_cadastro date,
  data_atualizacao date
);
 
CREATE TABLE  IF NOT EXISTS  api_animalx.animal(
  id BIGINT PRIMARY KEY  GENERATED ALWAYS AS IDENTITY,
  id_usuario BIGINT, 
  apelido  varchar(30),
  especie varchar(40) not null,
  raca varchar(40) ,
  descricao varchar(200),
  peso decimal,
  altura decimal,
  sexo varchar(15),
  idade int,
  categoria varchar(15),
  situacao varchar(15),
  logintude varchar(100),
  latitude varchar(100),
  status varchar(10),
  data_cadastro date,
  data_atualizacao date, 
  FOREIGN KEY (id_usuario) REFERENCES api_animalx.usuario(id) 
);  
CREATE TABLE  IF NOT EXISTS api_animalx.contato_animal (
  id BIGINT PRIMARY KEY  GENERATED ALWAYS AS IDENTITY,
  id_animal BIGINT, 
  email  varchar(100),
  telefone varchar(12) not null,
  cidade varchar(30) not null,
  estado varchar(10) not null,
  hash_animal varchar(100),
  data_cadastro date,
  data_atualizacao date, 
  FOREIGN KEY (id_animal) REFERENCES api_animalx.animal(id) 
)
CREATE TABLE  IF NOT EXISTS  api_animalx.foto(
  id BIGINT PRIMARY KEY  GENERATED ALWAYS AS IDENTITY,  
  id_animal BIGINT,
  nome  varchar(100),
  caminho varchar(100),
  tamanho varchar(10),
  contexto varchar(10),
  data_cadastro date,
  data_atualizacao date, 
  FOREIGN KEY (id_animal) REFERENCES api_animalx.animal (id) 
);