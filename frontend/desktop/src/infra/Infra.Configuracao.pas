unit Infra.Configuracao;

interface

type
  TConfiguracao = class
    private
      class var FUrlApi : String;
      class function CaminhoArquivo: string; static;
    public
      class function UrlApi: string; static;
  end;

implementation

uses
  System.SysUtils,
  System.IniFiles;

{ TConfiguracao }

class function TConfiguracao.CaminhoArquivo: string;
begin
  Result := ExtractFilePath(ParamStr(0)) + 'catalogo.ini';
end;

class function TConfiguracao.UrlApi: string;
var
  LIni : TIniFile;
begin

  if FUrlApi.IsEmpty then
  begin

    if not FileExists(CaminhoArquivo) then
      raise Exception.CreateFmt('Arquivo de configuração não encontrado: %s', [CaminhoArquivo]);

    LIni := TIniFile.Create(CaminhoArquivo);

    try
      FUrlApi := LIni.ReadString('api', 'url', '').Trim.TrimRight(['/']);
    finally
      LIni.Free;
    end;

    if FUrlApi.IsEmpty then
      raise Exception.Create('Informe a URL da API em catalogo.ini, seção [api], chave url.');

  end;

  Result := FUrlApi;

end;

end.
