unit Infra.Sessao;

interface

type
  TSessao = class
  private
    class var FEmail: string;
    class var FAccessToken: string;
    class var FRefreshToken: string;
  public
    class procedure Iniciar(const AEmail, AAccessToken, ARefreshToken: string); static;
    class procedure AtualizarTokens(const AAccessToken, ARefreshToken: string); static;
    class procedure Encerrar; static;
    class function Ativa: Boolean; static;
    class property Email: string read FEmail;
    class property AccessToken: string read FAccessToken;
    class property RefreshToken: string read FRefreshToken;
  end;

implementation

uses
  System.SysUtils;

{ TSessao }

class function TSessao.Ativa: Boolean;
begin

  Result := not FAccessToken.IsEmpty;

end;

class procedure TSessao.AtualizarTokens(const AAccessToken,
  ARefreshToken: string);
begin

  FAccessToken := AAccessToken;
  FRefreshToken := ARefreshToken;

end;

class procedure TSessao.Encerrar;
begin

  FEmail        := EmptyStr;
  FAccessToken  := EmptyStr;
  FRefreshToken := EmptyStr;

end;

class procedure TSessao.Iniciar(const AEmail, AAccessToken,
  ARefreshToken: string);
begin

  FEmail        := AEmail;
  FAccessToken  := AAccessToken;
  FRefreshToken := ARefreshToken;

end;

end.

