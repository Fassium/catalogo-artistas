unit Servico.Autenticacao;

interface

type
  TServicoAutenticacao = class
  public
    class procedure Entrar(const AEmail, ASenha: string); static;
  end;

implementation

uses
  System.JSON, Infra.Requisicao.Intf, Infra.Requisicao, Infra.Sessao;

{ TServicoAutenticacao }

class procedure TServicoAutenticacao.Entrar(const AEmail, ASenha: string);
var
  LCorpo: TJSONObject;
  LResposta: IResponse;
  LTokens: TJSONObject;
begin

  LCorpo := TJSONObject.Create;
  LCorpo.AddPair('email', AEmail);
  LCorpo.AddPair('senha', ASenha);

  LResposta := TRequisicao.Nova
    .Recurso('/v1/autenticacao/login')
    .AdicionarCorpo(LCorpo)
    .Post;

  LTokens := LResposta.JSONValue as TJSONObject;
  TSessao.Iniciar(
    AEmail,
    LTokens.GetValue<string>('accessToken'),
    LTokens.GetValue<string>('refreshToken'));

end;

end.

