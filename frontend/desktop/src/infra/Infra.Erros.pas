unit Infra.Erros;

interface

uses
  System.SysUtils, Infra.Requisicao.Intf;

type
  EErroApi = class(Exception)
    private
      FStatus : Integer;
    public
      constructor Create(const AStatus: Integer; const AMensagem: String);
      property Status: Integer read FStatus;
  end;

  TErros = class
    public
      class procedure Validar(const AResposta: IResponse); static;
  end;

implementation

uses System.JSON;

{ EErroApi }

constructor EErroApi.Create(const AStatus: Integer; const AMensagem: String);
begin
  inherited Create(AMensagem);
  FStatus := AStatus;
end;

{ TErros }

class procedure TErros.Validar(const AResposta: IResponse);
var
  LProblema: TJSONObject;
  LCampos: TJSONArray;
  LCampo: TJSONValue;
  LMensagem: string;
begin

  if (AResposta.StatusCode >= 200) and (AResposta.StatusCode <= 299) then
    Exit;

  LMensagem := Format('Erro %d ao falar com a API.', [AResposta.StatusCode]);

  if AResposta.JSONValue is TJSONObject then
  begin

    LProblema := TJSONObject(AResposta.JSONValue);
    LMensagem := LProblema.GetValue<string>('detail', LMensagem);

    if LProblema.TryGetValue<TJSONArray>('camposInvalidos', LCampos) then
      for LCampo in LCampos do
        LMensagem := LMensagem + sLineBreak + '- ' +
          LCampo.GetValue<string>('campo') + ': ' +
          LCampo.GetValue<string>('mensagem');

  end;

  raise EErroApi.Create(AResposta.StatusCode, LMensagem);

end;

end.
